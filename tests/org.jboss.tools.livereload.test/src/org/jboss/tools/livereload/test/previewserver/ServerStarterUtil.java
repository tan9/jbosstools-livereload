/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Xavier Coulon - Initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.livereload.test.previewserver;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerListener;
import org.eclipse.wst.server.core.ServerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xcoulon
 * 
 */
public class ServerStarterUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerStarterUtil.class);

	/**
	 * Starts the given {@link IServer} with a given
	 * 
	 * @param server
	 * @param timeout
	 * @param unit
	 * @throws CoreException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static void startServer(final IServer server, int timeout, TimeUnit unit) throws CoreException,
			InterruptedException, ExecutionException, TimeoutException {
		final ServerListener listener = new ServerListener();
		server.addServerListener(listener);
		LOGGER.info("Starting server {}", server.getName());
		server.start(ILaunchManager.RUN_MODE, new NullProgressMonitor());
		Future<?> future = Executors.newSingleThreadExecutor().submit(new Runnable() {
			
			@Override
			public void run() {
				while(!listener.isStarted()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						LOGGER.error("Failed to sleep", e);
					}
				}
				
			}
		});
		future.get(timeout, unit);
	}

	public static class ServerListener implements IServerListener {

		private boolean started = false;

		@Override
		public void serverChanged(ServerEvent event) {
			LOGGER.info("Server state changed: {} is now {}", event.getServer().getName(), event.getServer()
					.getServerState());
			setStarted(event.getServer().getServerState() == IServer.STATE_STARTED);
		}

		/**
		 * @return the started
		 */
		public boolean isStarted() {
			return started;
		}

		private void setStarted(boolean started) {
			this.started = started;
		}

	}
}