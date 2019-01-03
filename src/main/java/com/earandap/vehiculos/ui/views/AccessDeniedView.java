/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.earandap.vehiculos.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * View that is shown when the user attempts to access a restricted view.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringComponent
@UIScope
@SpringView(name = AccessDeniedView.VIEW_NAME)
public class AccessDeniedView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "accessDeniedView";

    private Label message;

    public AccessDeniedView() {
        setMargin(true);
        addComponent(message = new Label());
        message.setSizeUndefined();
        message.addStyleName(ValoTheme.LABEL_FAILURE);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        message.setValue(String.format("You do not have access to this resource: %s", event.getViewName()));
    }
}
