package com.camel.example3;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ProducerAndConsumerExample {

    public static void main(String[] args) throws Exception {
        CamelContext ctx = new DefaultCamelContext();
        ctx.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                // do what you want...
                                System.out.println("You're in the processor");
                                String message = exchange.getIn().getBody(String.class);
                                message += "-By Processor";
                                exchange.getOut().setBody(message);
                            }
                        })
                        .to("seda:end");
            }
        });

        ctx.start();

        ProducerTemplate producerTemplate = ctx.createProducerTemplate();
        producerTemplate.sendBody("direct:start", "Hello Camel");

        ConsumerTemplate consumerTemplate = ctx.createConsumerTemplate();
        String message = consumerTemplate.receiveBody("seda:end", String.class);
        System.out.println(message);
        ctx.stop();
    }
}
