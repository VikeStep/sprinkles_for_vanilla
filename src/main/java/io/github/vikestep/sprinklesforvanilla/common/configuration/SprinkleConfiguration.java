package io.github.vikestep.sprinklesforvanilla.common.configuration;

import io.github.vikestep.sprinklesforvanilla.common.reference.ModInfo;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

class SprinkleConfiguration extends Configuration
{

    //The config description is text that goes before configStart. It will not be regenerated if edited, but will be created if the separator does not exist
    //This also means that if the user tampers with this line, the config will break.
    private List<String> configDescription;
    private static final String DESCRIPTION_SEPARATOR = "--------------------------------------------------";

    public SprinkleConfiguration(File file)
    {
        super(file, ModInfo.VERSION);
    }

    @Override
    public void load()
    {
        //This will remove the description from the config file while the config is loaded
        File file = getConfigFile();
        boolean hasDescription = false;
        try
        {
            if (file.canRead() && file.canWrite())
            {
                List<String> configLines = FileUtils.readLines(file);
                int index = configLines.indexOf(DESCRIPTION_SEPARATOR);
                if (index != -1)
                {
                    hasDescription = true;
                    //If there is already a description there we want to overwrite whatever we already have there even if its default
                    configDescription = configLines.subList(0, index + 1);
                    FileUtils.writeLines(file, configLines.subList(index + 1, configLines.size()));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        super.load();

        //Put the description which was there before back again
        try
        {
            if (file.canRead() && file.canWrite() && hasDescription)
            {
                List<String> configLines = FileUtils.readLines(file);
                FileUtils.writeLines(file, configDescription);
                //The third parameter means that the configLines are appended
                FileUtils.writeLines(file, configLines, true);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void save()
    {
        super.save();
        File file = getConfigFile();
        try
        {
            if (file.canRead() && file.canWrite() && configDescription != null)
            {
                List<String> configLines = FileUtils.readLines(file);
                FileUtils.writeLines(file, configDescription);
                //The third parameter means that the configLines are appended
                FileUtils.writeLines(file, configLines, true);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setConfigDescription(List<String> configDescription)
    {
        if (configDescription != null && this.configDescription == null)
        {
            this.configDescription = configDescription;
        }
    }
}
