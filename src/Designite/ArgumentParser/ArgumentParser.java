package Designite.ArgumentParser;

import org.apache.commons.cli.Option;


public abstract class ArgumentParser {
    /**
     * {@code createRequiredOption}. A method to initialise required {@link Option}.
     * @param shortOpt
     * @param longOpt
     * @param description
     * @return
     */
    Option createRequiredOption(String shortOpt, String longOpt, String description) {
        Option option = new Option(shortOpt, longOpt, true, description);
        option.setRequired(true);
        return option;
    }

    /**
     * {@code parseArguments} converts the appropriate {@code args} parameter from the system.
     * It extracts the data from system arguments.
     * @param args
     * @return
     */
    public abstract InputArgs parseArguments(String[] args);



}
