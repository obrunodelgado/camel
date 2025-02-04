/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.xslt;

import net.sf.saxon.trans.XPathException;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SaxonXsltFeatureRouteTest extends CamelTestSupport {

    @Test
    public void testSendMessage() throws Exception {
        String message = "<hello/>";
        sendXmlMessage("direct:start1", message);
        sendXmlMessage("direct:start2", message);
    }

    public void sendXmlMessage(String uri, String message) {
        try {
            template.sendBody("direct:start1", message);
            fail("expect an exception here");
        } catch (Exception ex) {

            assertTrue(ex instanceof CamelExecutionException);
            assertTrue(ex.getCause() instanceof XPathException);

        }

    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start1")
                        .to("xslt-saxon:org/apache/camel/component/xslt/transform_text_imported.xsl")
                        .to("mock:result");

                from("direct:start2")
                        .to("xslt-saxon:org/apache/camel/component/xslt/transform_text.xsl")
                        .to("mock:result");
            }
        };
    }

}
