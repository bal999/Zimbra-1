<!--
 * ***** BEGIN LICENSE BLOCK *****
 * 
 * Zimbra Collaboration Suite Zimlets
 * Copyright (C) 2010 Zimbra, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * 
 * ***** END LICENSE BLOCK *****
-->
<%@ page import="java.io.*" %>
<%@ page import="com.zimbra.common.localconfig.*" %>
<%
    final String ZIMLET = "zimbra_inboxzero";
    final String FILENAME = "SetMessageDateFoReelz.jsp";

    ServletContext zimletContext = getServletContext();
    ServletContext serviceContext = zimletContext.getContext("/service");

    String servicePath = serviceContext.getRealPath("/");
    File odir = new File(new File(servicePath), ZIMLET);
    File ofile = new File(odir, FILENAME);

    // copy real JSP to /service webapp
    synchronized (this) {
        if (!ofile.exists()) {
            if (!odir.exists()) {
                odir.mkdirs();
            }

            String zimletPath = LC.zimlet_directory.value();
            File idir = new File(new File(zimletPath), ZIMLET);
            File ifile = new File(idir, FILENAME);

            InputStream istream = null;
            OutputStream ostream = null;
            try {
                istream = new FileInputStream(ifile);
                ostream = new FileOutputStream(ofile);

                byte[] buffer = new byte[4096];
                int count;
                while ((count = istream.read(buffer)) != -1) {
                    ostream.write(buffer, 0, count);
                }
            }
            finally {
                try { istream.close(); } catch (Exception e) { /* ignore */ }
                try { ostream.close(); } catch (Exception e) { /* ignore */ }
            }
        }
    }

    // forward to real JSP
    final String REAL_JSP = "/"+ZIMLET+"/"+FILENAME;
    RequestDispatcher dispatcher = serviceContext.getRequestDispatcher(REAL_JSP);
    dispatcher.forward(request, response);
%>