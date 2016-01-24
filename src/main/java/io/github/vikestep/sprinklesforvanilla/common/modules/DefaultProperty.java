package io.github.vikestep.sprinklesforvanilla.common.modules;

public class DefaultProperty extends PropertyBase

{
    private final Object defaultValue;
    private       Object value;

    public DefaultProperty(Type type, String name, String description, boolean requiresWorldRestart, boolean requiresMcRestart, Object defaultValue)
    {
        super(type, name, description, requiresWorldRestart, requiresMcRestart);
        this.defaultValue = defaultValue;
    }

    @Override
    public Object getDefault()
    {
        return defaultValue;
    }

    @Override
    public Object getValue()
    {
        return value;
    }

    @Override
    public void setValue(Object object)
    {
        value = object;
    }
}
