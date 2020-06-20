package com.xuanfeng.testcomponent.hotfix;

public class SayException implements ISay {
    @Override
    public String saySomething() {
        return "出错误了！";
    }
}
