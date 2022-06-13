package com.camel.example5;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;

public class CallMethodUsingClassComponent {
    public static void main(String[] args) throws Exception {

        MyService myService = new MyService();

        SimpleRegistry registry = new SimpleRegistry();
        registry.put("myService", myService);

        CamelContext ctx = new DefaultCamelContext(registry);

        ctx.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                //from("direct:start").to("class:com.camel.example5.MyService?method=doSomething");
                from("direct:start").to("bean:myService?method=doSomething");
            }
        });

        ctx.start();

        ProducerTemplate producerTemplate = ctx.createProducerTemplate();
        producerTemplate.sendBody("direct:start", "Hello");

        ctx.stop();
    }
}
