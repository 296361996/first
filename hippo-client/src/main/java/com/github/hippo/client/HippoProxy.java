package com.github.hippo.client;

import java.lang.reflect.Proxy;
import java.util.UUID;

import com.github.hippo.cache.MsgThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.hippo.annotation.HippoService;
import com.github.hippo.bean.HippoRequest;
import com.github.hippo.bean.HippoResponse;
import com.github.hippo.govern.ServiceGovern;
import com.github.hippo.netty.HippoClientBootstrap;
import com.github.hippo.netty.HippoResultCallBack;

/**
 * client代理类
 * 
 * @author sl
 *
 */
@Component
public class HippoProxy {

  @Autowired
  private ServiceGovern serviceGovern;

  @Value("${hippo.read.timeout:3}")
  private int hippoReadTimeout;
  @Value("${hippo.needTimeout:false}")
  private boolean needTimeout;

  @SuppressWarnings("unchecked")
  <T> T create(Class<?> inferfaceClass) {
    return (T) Proxy.newProxyInstance(inferfaceClass.getClassLoader(),
        new Class<?>[] {inferfaceClass}, (proxy, method, args) -> {
          HippoRequest request = new HippoRequest();
          request.setRequestId(UUID.randomUUID().toString());
          request.setMsgId(MsgThreadLocal.Instance.getMsgId());
          request.setMsgLevel(MsgThreadLocal.Instance.getMsgLevel());
          request.setRequestType(0);
          request.setClassName(method.getDeclaringClass().getName());
          request.setMethodName(method.getName());
          request.setParameterTypes(method.getParameterTypes());
          request.setParameters(args);
          return getHippoResponse(inferfaceClass.getAnnotation(HippoService.class).serviceName(),
              request);
        });
  }

  private Object getHippoResponse(String serviceName, HippoRequest request) throws Throwable {
    HippoClientBootstrap hippoClientBootstrap = HippoClientBootstrap.getBootstrap(serviceName,
        hippoReadTimeout, needTimeout, serviceGovern);
    HippoResultCallBack callback = hippoClientBootstrap.sendAsync(request);
    HippoResponse result = callback.getResult();
    if (result.isError()) {
      throw result.getThrowable();
    }
    return result.getResult();
  }

  /**
   * api request
   * 
   * @param serviceHost
   * @param serviceMethod
   * @param parameter
   * @return
   * @throws Throwable
   */
  public Object apiRequest(String serviceHost, String serviceMethod, Object parameter)
      throws Throwable {
    String[] serviceMethods = serviceMethod.split("/");
    Object[] objects = new Object[1];
    objects[0] = parameter;
    HippoRequest request = new HippoRequest();
    request.setRequestId(UUID.randomUUID().toString());
    request.setMsgId(MsgThreadLocal.Instance.getMsgId());
    request.setMsgLevel(MsgThreadLocal.Instance.getMsgLevel());
    request.setRequestType(1);
    request.setClassName(serviceMethods[0]);
    request.setMethodName(serviceMethods[1]);
    request.setParameterTypes(null);
    request.setParameters(objects);
    return getHippoResponse(serviceHost, request);
  }
}
