package com.zimbra.qa.selenium.projects.desktop.ui.accounts;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.GeneralUtility;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.desktop.ui.AppAjaxClient;
import com.zimbra.qa.selenium.projects.desktop.ui.PageLogin;

public class PageAddNewAccount extends AbsTab{

   public static class Locators {
      public static final String zAccountDropDown = "css=select[id='accountFlavor']";
      public static final String zSelectAccountTypeOption = zAccountDropDown + " option[value='']";
      public static final String zZimbraAccountOption = zAccountDropDown + " option[value='Zimbra']";
      public static final String zGmailAccountOption = zAccountDropDown + " option[value='Gmail']";
      public static final String zYahooAccountOption = zAccountDropDown + " option[value='YMP']";
      public static final String zMicrosoftExchangeIMAPOption = zAccountDropDown + " option[value='MSE']";
      public static final String zIMAPOption = zAccountDropDown + " option[value='Imap']";
      public static final String zPopOption = zAccountDropDown + " option[value='Pop']";
   }

   public enum DROP_DOWN_OPTION {
      ZIMBRA,
      GMAIL,
      YAHOO,
      MICROSOFT_EXCHANGE_IMAP,
      IMAP,
      POP
   }

   public PageAddNewAccount(AbsApplication application) {
      super(application);
      logger.info("new " + PageAddNewAccount.class.getCanonicalName());
   }


   public AbsPage zDropDownListSelect(DROP_DOWN_OPTION option) throws HarnessException {

      String valueLocator = null;
      AbsPage result = null;

      switch (option) {
      case ZIMBRA:
         valueLocator = "value=Zimbra";
         result = new FormAddZimbraAccount(MyApplication);
         break;
      case GMAIL:
         valueLocator = "value=Gmail";
         break;
      case IMAP:
         valueLocator = "value=Imap";
         break;
      case MICROSOFT_EXCHANGE_IMAP:
         valueLocator = "value=MSE";
         break;
      case POP:
         valueLocator = "value=Pop";
         break;
      case YAHOO:
         valueLocator = "value=YMP";
         break;
      default:
         throw new HarnessException("Impelement me!");
      }

      GeneralUtility.waitForElementPresent(this, Locators.zAccountDropDown);
      sSelectDropDown(Locators.zAccountDropDown, valueLocator);

      if (result != null){
         result.zWaitForActive();
      }
      return result;
   }

   @Override
   public AbsPage zListItem(Action action, String item) throws HarnessException {
      throw new HarnessException("Add New Account page does not have lists");
   }

   @Override
   public AbsPage zListItem(Action action, Button option, String item)
         throws HarnessException {
      throw new HarnessException("Add New Account page does not have lists");
   }

   @Override
   public AbsPage zListItem(Action action, Button option, Button subOption,
         String item) throws HarnessException {
      throw new HarnessException("Add New Account page does not have lists");
   }

   @Override
   public void zNavigateTo() throws HarnessException {

      if (zIsActive()) {
         logger.info("Add New Account page is already active");
      } else {
         ((AppAjaxClient)MyApplication).zPageLogin.zNavigateTo();
         
         String locator = PageLogin.Locators.zAddNewAccountButton;
         GeneralUtility.waitForElementPresent(this, locator);
         sClick(locator);
         GeneralUtility.waitForElementPresent(this, Locators.zAccountDropDown);
      }
   }

   @Override
   public AbsPage zToolbarPressButton(Button button) throws HarnessException {
      throw new HarnessException("Add New Account page does not have a Toolbar");
   }

   @Override
   public AbsPage zToolbarPressPulldown(Button pulldown, Button option)
         throws HarnessException {
      throw new HarnessException("Add New Account page does not have a Toolbar");
   }

   @Override
   public String myPageName() {
      return (this.getClass().getName());
   }

   @Override
   public boolean zIsActive() throws HarnessException {
      if (!sIsElementPresent(Locators.zAccountDropDown)) {
         logger.info("Account Drop down list is not present");
         return false;
      } else {
         logger.info("Account Drop down list is present");
      }

      if (!zIsVisiblePerPosition(Locators.zAccountDropDown, 0, 0)) {
         logger.info("Account Drop down list is not visible per position (0, 0)");
         return false;
      } else {
         logger.info("Account Drop down list is visible per position (0, 0)");
      }

      return true;
   }

}
