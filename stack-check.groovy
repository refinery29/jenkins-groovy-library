build = Thread.currentThread().executable
env = build.getEnvironment()
stack = env.get('stack')
stack_name = env.get('stack_name')

def interrupt(message) {
    msg = 'ERROR: ' + message
    build.displayName = msg
    println(msg)
    throw new InterruptedException(msg)
}

// Abort the build if no stack set.
if (stack == '' && stack_name == '') {
    interrupt('No stack specified')
}

// Abort if both stack and stack name are provided.
if (stack != '' && stack_name != '') {
    interrupt('Both stack and stack_name provided')
}
