build = Thread.currentThread().executable
env = build.getEnvironment()

def set_build_display_name(text) {
    /**
     * The text argument is a string which the displayName of the build
     * will be set to. It may contain {parameter}, which will be replaced
     * with the value of the specified parameter.
     */
    matches = (text =~ /\{([^\}]+)\}/)

    matches.each {
        text.replace(it[0], env.get(it[1]))
    }
    return text
