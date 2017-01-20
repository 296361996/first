package com.github.hippo.bean;

import java.io.Serializable;


/**
 * 
 * 返回结果包装类
 * 
 * @author sl
 *
 */
public class HippoResponse implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -2553246569725890161L;
  private String requestId;
  private String msgId;
  private int msgLevel;
  private Object result;
  private Throwable throwable;
  private boolean isError = false;// default if true result is HippoRequest for trace request param

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public int getMsgLevel() {
    return msgLevel;
  }

  public void setMsgLevel(int msgLevel) {
    this.msgLevel = msgLevel;
  }

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  public Throwable getThrowable() {
    return throwable;
  }

  public void setThrowable(Throwable throwable) {
    this.throwable = throwable;
  }

  public boolean isError() {
    return isError;
  }

  public void setError(boolean isError) {
    this.isError = isError;
  }

  @Override
  public String toString() {
    return "HippoResponse{" +
            "requestId='" + requestId + '\'' +
            ", msgId='" + msgId + '\'' +
            ", msgLevel=" + msgLevel +
            ", result=" + result +
            ", throwable=" + throwable +
            ", isError=" + isError +
            '}';
  }
}
