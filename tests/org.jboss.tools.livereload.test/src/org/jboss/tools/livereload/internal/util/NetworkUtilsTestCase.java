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

package org.jboss.tools.livereload.internal.util;

import static org.fest.assertions.Assertions.assertThat;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * @author xcoulon
 *
 */
public class NetworkUtilsTestCase {

	@Test
	public void shouldRetrieveInetAddresses() throws SocketException {
		// pre-condition
		// operation
		final Map<String, List<InetAddress>> networkInterfaces = NetworkUtils.retrieveNetworkInterfaces();
		// verification
		assertThat(networkInterfaces).isNotEmpty();
	}
}