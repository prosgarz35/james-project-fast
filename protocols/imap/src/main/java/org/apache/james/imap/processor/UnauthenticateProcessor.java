/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package org.apache.james.imap.processor;

import jakarta.inject.Inject;

import org.apache.james.imap.api.message.response.StatusResponseFactory;
import org.apache.james.imap.api.process.ImapSession;
import org.apache.james.imap.message.request.UnauthenticateRequest;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.metrics.api.MetricFactory;
import org.apache.james.util.MDCBuilder;

import reactor.core.publisher.Mono;

/**
 * Processor which implements the UNAUTHENTICATE command according to RFC 9051 section 6.2.4.
 */
public class UnauthenticateProcessor extends AbstractMailboxProcessor<UnauthenticateRequest> {

    @Inject
    public UnauthenticateProcessor(MailboxManager mailboxManager, StatusResponseFactory factory,
                                  MetricFactory metricFactory) {
        super(UnauthenticateRequest.class, mailboxManager, factory, metricFactory);
    }

    @Override
    protected Mono<Void> processRequestReactive(UnauthenticateRequest request, ImapSession session, Responder responder) {
        return session.unauthenticate()
            .then(Mono.fromRunnable(() -> okComplete(request, responder)));
    }

    @Override
    protected MDCBuilder mdc(UnauthenticateRequest request) {
        return MDCBuilder.create()
            .addToContext(MDCBuilder.ACTION, "UNAUTHENTICATE");
    }
}
