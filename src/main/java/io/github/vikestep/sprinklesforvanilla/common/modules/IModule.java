package io.github.vikestep.sprinklesforvanilla.common.modules;

import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

//Modules should be implemented as singletons
public interface IModule
{
    // Returns a list of properties this module has (in order)
    List<IProperty> getProperties();

    // Returns the name of the module for use in the config
    String getModuleName();

    // Returns the side of the module for use in the config
    Side getModuleSide();

    // Enables the module code so it is ran
    void enable();

    // Disables the module code so it is not ran
    void disable();

    // Initialisation code
    @SuppressWarnings("EmptyMethod")
    void init();

    // Get whether or not the module is currently enabled
    boolean isEnabled();

    // Sets whether or not the module is currently enabled
    void setEnabled(boolean isEnabled);
}
