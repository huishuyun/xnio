/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.xnio.nio;

import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import org.xnio.Xnio;

final class SelectorUtils {
    private SelectorUtils() {
    }

    public static void await(NioXnio nioXnio, SelectableChannel channel, int op) throws IOException {
        if (! Xnio.isBlockingAllowed()) {
            throw new SecurityException("Blocking I/O is not allowed on the current thread");
        }
        final Selector selector = nioXnio.getSelector();
        final SelectionKey selectionKey = channel.register(selector, op);
        selector.select();
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedIOException();
        }
        selectionKey.interestOps(0);
    }

    public static void await(NioXnio nioXnio, SelectableChannel channel, int op, long time, TimeUnit unit) throws IOException {
        if (! Xnio.isBlockingAllowed()) {
            throw new SecurityException("Blocking I/O is not allowed on the current thread");
        }
        final Selector selector = nioXnio.getSelector();
        final SelectionKey selectionKey = channel.register(selector, op);
        selector.select(unit.toMillis(time));
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedIOException();
        }
        selectionKey.interestOps(0);
    }
}
