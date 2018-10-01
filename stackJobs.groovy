println("Running ${getClass().protectionDomain.codeSource.location.path}")
println("Running in dir ${new File(getClass().protectionDomain.codeSource.location.path).getParentFile()}")
evaluate(new File("${getClass().protectionDomain.codeSource.location.path.getParentFile()}/jenkinsUtility.groovy"))
println("Running ${getClass().protectionDomain.codeSource.location.path}")
build = Thread.currentThread().executable
env = build.getEnvironment()
stack = env.get('stack')
stack_name = env.get('stack_name')
abort_unless_valid_stack_config(build, stack, stack_name)
if (env.get('build_label') == '') {
    setBuildParameters(['build_label': get_build_label_for_stack(stack)])
}
set_build_display_for_stack(stack, stack_name, env.get('neptune_ref'))