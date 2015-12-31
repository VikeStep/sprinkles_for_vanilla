package io.github.vikestep.sprinklesforvanilla.common.modules;

public abstract class PropertyBase implements IProperty
{
    Type type;
    String name;
    String description;
    boolean requiresWorldRestart;
    boolean requiresMcRestart;

    public PropertyBase(Type type, String name, String description, boolean requiresWorldRestart, boolean requiresMcRestart)
    {
        this.type = type;
        this.name = name;
        this.description = description;
        this.requiresWorldRestart = requiresWorldRestart;
        this.requiresMcRestart = requiresMcRestart;
    }

    @Override
    public Type getType()
    {
        return type;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public boolean requiresWorldRestart()
    {
        return requiresWorldRestart;
    }

    @Override
    public boolean requiresMcRestart()
    {
        return requiresMcRestart;
    }
}
