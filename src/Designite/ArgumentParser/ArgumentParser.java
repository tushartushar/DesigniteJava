package Designite.ArgumentParser;

import Designite.llm.LLMConfig;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Arrays;

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

    void addSharedOptions(Options options) {
        options.addOption("llm", "enable-llm", false, "Enable LLM-based comment analysis");
    }

    void handleSharedOptions(CommandLine cmd) {
        if (cmd.hasOption("llm")) {
            LLMConfig.enable();
        }
    }

    String[] normalizeArgs(String[] args) {
        String[] normalizedArgs = Arrays.copyOf(args, args.length);
        for (int i = 0; i < normalizedArgs.length; i++) {
            if ("--llm".equalsIgnoreCase(normalizedArgs[i])) {
                normalizedArgs[i] = "-llm";
            }
        }
        return normalizedArgs;
    }

    /**
     * {@code parseArguments} converts the appropriate {@code args} parameter from the system.
     * It extracts the data from system arguments.
     * @param args
     * @return
     */
    public abstract InputArgs parseArguments(String[] args);



}
