package com.dynamo.cr.ddfeditor.wizards;

import com.dynamo.cr.editor.ui.AbstractNewDdfWizard;


public class LightNewWizard extends AbstractNewDdfWizard {
    @Override
    public String getTitle() {
        return "Light file";
    }

    @Override
    public String getDescription() {
        return "This wizard creates a new light file.";
    }

    @Override
    public String getExtension() {
        return "light";
    }

}