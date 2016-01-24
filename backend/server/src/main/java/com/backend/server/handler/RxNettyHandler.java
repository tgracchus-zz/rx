package com.backend.server.handler;

import com.backend.server.endpoint.Endpoints;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.nio.charset.Charset;


public class RxNettyHandler implements RequestHandler<ByteBuf, ByteBuf> {


    private final Endpoints endpoints;

    public RxNettyHandler(Endpoints endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
       return request.getContent()
                .map(byteBuf -> {
                    String body = byteBuf.toString(Charset.defaultCharset());
                    return new Request(request, request.getHttpMethod(), body);

                })
                .flatMap((Func1<Request, Observable<Response>>)
                        wrappedRequest -> endpoints.findEndpoint(request.getUri(), request.getHttpMethod())
                                .map(endpoint -> endpoint.call(wrappedRequest))
                                .orElse(Observable.just(new Response(HttpResponseStatus.BAD_REQUEST, wrappedRequest, "{}")))
                )

                .flatMap(wrappedResponse -> {
                    response.setStatus(wrappedResponse.getStatus());
                    response.writeStringAndFlush(wrappedResponse.getBody());
                    return response.close();
                });

     }
}




