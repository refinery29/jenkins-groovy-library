build = Thread.currentThread().executable
env = build.getEnvironment()
stack = env.get('stack')
stack_name = env.get('stack_name')

// Abort the build if no stack set.
if (stack == '' && stack_name == '') {
    build.displayName = 'No stack specified'
    throw new InterruptedException('Aborting due to missing stack')
}

// Abort if both stack and stack name are provided.
if (stack != '' && stack_name != '') {
  build.displayName = "ERROR: stack and stack_name provided"
  throw new InterruptedException("ERROR: Only one of `stack` and `stack_name` should be provided")
}
