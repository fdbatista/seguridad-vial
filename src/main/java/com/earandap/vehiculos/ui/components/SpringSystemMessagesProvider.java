package com.earandap.vehiculos.ui.components;

import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;

public class SpringSystemMessagesProvider implements SystemMessagesProvider {

    @Override
    public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
        CustomizedSystemMessages messages = new CustomizedSystemMessages();
        messages.setSessionExpiredCaption("Sesi\u00F3n caducada");
        messages.setSessionExpiredMessage("Su sesi\u00F3n ha caducado. Por favor, introduzca nuevamente sus credenciales.");
        messages.setCookiesDisabledCaption("Cookies deshabilitadas");
        messages.setCookiesDisabledMessage("Por favor, habilite las cookies en su navegador.");
        messages.setCommunicationErrorCaption("Error de comunicaci\u00F3n");
        messages.setCommunicationErrorMessage("Recargue la p\u00E1gina para actualizar su contenido");
        messages.setCommunicationErrorNotificationEnabled(true);
        return messages;
    }
    
}
