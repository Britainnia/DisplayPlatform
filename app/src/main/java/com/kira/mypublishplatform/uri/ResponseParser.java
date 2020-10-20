package com.kira.mypublishplatform.uri;

import com.kira.mypublishplatform.bean.ResponseBean;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Response;
import rxhttp.wrapper.annotation.Parser;
import rxhttp.wrapper.entity.ParameterizedTypeImpl;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.parse.AbstractParser;

//通过@Parser注解，为解析器取别名为Response，此时就会在RxHttp类生成asResponse(Class<T>)方法
@Parser(name = "Response")
public class ResponseParser<T> extends AbstractParser<T> {

    //注意，以下两个构造方法是必须的
    protected ResponseParser() { super(); }
    public ResponseParser(Type type) { super(type); }

    //省略构造方法
    @Override
    public T onParse(Response response) throws IOException {
        final Type type = ParameterizedTypeImpl.get(ResponseBean.class, mType); //获取泛型类型
        ResponseBean<T> data = convert(response, type);
        T t = data.getData(); //获取data字段
        if (t == null) {//这里假设code不等于0，代表数据不正确，抛出异常
            throw new ParseException(String.valueOf(data.getCode()), data.getMsg(), response);
        }
        return t;
    }
}

