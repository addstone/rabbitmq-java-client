//  The contents of this file are subject to the Mozilla Public License
//  Version 1.1 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License
//  at http://www.mozilla.org/MPL/
//
//  Software distributed under the License is distributed on an "AS IS"
//  basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
//  the License for the specific language governing rights and
//  limitations under the License.
//
//  The Original Code is RabbitMQ.
//
//  The Initial Developer of the Original Code is VMware, Inc.
//  Copyright (c) 2007-2011 VMware, Inc.  All rights reserved.
//


package com.rabbitmq.client.impl;

import java.io.DataOutputStream;
import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQImpl.MethodVisitor;

/**
 * Base class for AMQP method objects, specialized by autogenerated
 * code in AMQP.java.
 */
public abstract class Method implements com.rabbitmq.client.Method {
    /** {@inheritDoc} */
    public abstract int protocolClassId(); /* properly an unsigned short */

    /** {@inheritDoc} */
    public abstract int protocolMethodId(); /* properly an unsigned short */

    /** {@inheritDoc} */
    public abstract String protocolMethodName();

    /**
     * Tell if content is present.
     * @return true if the wire-protocol for this method should involve a content header and body,
     * or false if it should just involve a single method frame.
     */
    public abstract boolean hasContent();

    /**
     * Visitor support (double-dispatch mechanism).
     * @param visitor the visitor object
     * @return the result of the "visit" operation
     * @throws IOException if an error is encountered
     */
    public abstract Object visit(MethodVisitor visitor) throws IOException;

    /**
     * Private API - Autogenerated writer for this method.
     * @param writer interface to an object to write the method arguments
     * @throws IOException if an error is encountered
     */
    public abstract void writeArgumentsTo(MethodArgumentWriter writer) throws IOException;

    /**
     * Public API - debugging utility
     * @param buffer the buffer to append debug data to
     */
    public void appendArgumentDebugStringTo(StringBuffer buffer) {
        buffer.append("(?)");
    }

    @Override public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("#method<").append(protocolMethodName()).append(">");
        this.appendArgumentDebugStringTo(sb);
        return sb.toString();
    }

    public Frame toFrame(int channelNumber) throws IOException {
        Frame frame = new Frame(AMQP.FRAME_METHOD, channelNumber);
        DataOutputStream bodyOut = frame.getOutputStream();
        bodyOut.writeShort(protocolClassId());
        bodyOut.writeShort(protocolMethodId());
        MethodArgumentWriter argWriter = new MethodArgumentWriter(bodyOut);
        writeArgumentsTo(argWriter);
        argWriter.flush();
        return frame;
    }
}
