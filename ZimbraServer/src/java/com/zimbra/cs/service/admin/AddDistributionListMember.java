/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2005, 2006, 2007, 2009, 2010 Zimbra, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.cs.service.admin;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.zimbra.cs.account.AccountServiceException;
import com.zimbra.cs.account.DistributionList;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.account.ShareInfo;
import com.zimbra.cs.account.Provisioning.DistributionListBy;
import com.zimbra.cs.account.accesscontrol.AdminRight;
import com.zimbra.cs.account.accesscontrol.Rights.Admin;
import com.zimbra.cs.mailbox.OperationContext;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.ZimbraLog;
import com.zimbra.common.soap.AdminConstants;
import com.zimbra.common.soap.Element;
import com.zimbra.soap.ZimbraSoapContext;

public class AddDistributionListMember extends AdminDocumentHandler {

    /**
     * must be careful and only allow access to domain if domain admin
     */
    public boolean domainAuthSufficient(Map context) {
        return true;
    }
    
    public Element handle(Element request, Map<String, Object> context) throws ServiceException {
        
        ZimbraSoapContext zsc = getZimbraSoapContext(context);
        OperationContext octxt = getOperationContext(zsc, context);
        Provisioning prov = Provisioning.getInstance();
        
        String id = request.getAttribute(AdminConstants.E_ID);
        List<String> memberList = new LinkedList<String>();
        for (Element elem : request.listElements(AdminConstants.E_DLM)) {
        	memberList.add(elem.getTextTrim());
        }
        if (memberList.isEmpty()) {
            throw ServiceException.INVALID_REQUEST("members to add not specified", null);
        }
        
        DistributionList dl = prov.get(DistributionListBy.id, id);
        if (dl == null)
            throw AccountServiceException.NO_SUCH_DISTRIBUTION_LIST(id);

        checkDistributionListRight(zsc, dl, Admin.R_addDistributionListMember);

        String[] members = (String[]) memberList.toArray(new String[0]); 
        prov.addMembers(dl, members);
        ZimbraLog.security.info(ZimbraLog.encodeAttrs(
                    new String[] {"cmd", "AddDistributionListMember","name", dl.getName(), "members", Arrays.deepToString(members)})); 
        
        // send share notification email
        boolean sendShareInfoMsg = dl.getBooleanAttr(Provisioning.A_zimbraDistributionListSendShareMessageToNewMembers, true);
        if (sendShareInfoMsg)
            ShareInfo.NotificationSender.sendShareInfoMessage(octxt, dl, members);
        
        Element response = zsc.createElement(AdminConstants.ADD_DISTRIBUTION_LIST_MEMBER_RESPONSE);
        return response;
    }
    
    @Override
    public void docRights(List<AdminRight> relatedRights, List<String> notes) {
        relatedRights.add(Admin.R_addDistributionListMember);
    }
}
