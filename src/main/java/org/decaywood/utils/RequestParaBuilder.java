package org.decaywood.utils;

/**
 * 请求参数构造器
 * @author: decaywood
 * @date: 2015/11/23 16:57
 */
public class RequestParaBuilder {

    //构造的url请求后缀
    private StringBuilder config;

    public RequestParaBuilder(String target) {
        this.config = new StringBuilder(target);
        this.config.append('?');
    }

    public RequestParaBuilder addParameter(String key, String val) {
        this.config.append(key).append("=").append(val).append("&");
        return this;
    }

    public RequestParaBuilder addParameter(String key, boolean val) {
        this.config.append(key).append("=").append(val).append("&");
        return this;
    }

    public RequestParaBuilder addParameter(String key, int val) {
        this.config.append(key).append("=").append(val).append("&");
        return this;
    }

    public RequestParaBuilder addParameter(String key, long val) {
        this.config.append(key).append("=").append(val).append("&");
        return this;
    }

    public String build() {
        return this.config.substring(0, config.length() - 1);
    }

}
