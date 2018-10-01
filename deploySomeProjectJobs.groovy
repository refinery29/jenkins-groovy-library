println("Running ${getClass().protectionDomain.codeSource.location.path}")

import hudson.model.StringParameterValue
import hudson.model.ParametersAction

def setBuildParameters(map) {
  def new_parameter_list = new ArrayList<StringParameterValue>()
  for (item in map) {
    new_parameter_list.add(
    new StringParameterValue(
    item.key.toString(),
    item.value.toString()))
  }
  def newParameters = null
  def oldParameters = build.getAction(ParametersAction.class)
  if (oldParameters != null) {
    build.actions.remove(oldParameters)
      newParameters = oldParameters.createUpdated(new_parameter_list)
  } else {
    newParameters = new ParametersAction(new_parameter_list)
  }
  build.actions.add(newParameters)
}

def get_build_label_for_stack(stack) {
  switch (stack) {
    case ~/(PRODUCTION|preprod(\d\d)?|stack\d\d)/:
      return 'dal5'
    default:
      return 'cloud'
  }
}

def set_build_display_for_deploy(gitref, stack, stack_name, neptune_ref) {
  build.displayName = "${gitref.take(8)}>${stack}${stack_name}"
  build.description = "neptune_ref: ${neptune_ref}"
}

def abort_build_if_missing_parameters(build, parameter_names) {
  env = build.getEnvironment()

  parameter_values = parameter_names.collect { env.get(it) }
  parameter_values - '' // Remove empty values

  if (! parameter_values) {
    build.displayName = "Missing value for ${parameter_names.join(', ')}."
    throw new InterruptedException('Aborting due to missing stack.')
  }

  return
}

build = Thread.currentThread().executable
env = build.getEnvironment()

abort_build_if_missing_parameters(build, ['stack', 'stack_name'])

stack = env.get('stack')

setBuildParameters(['build_label': get_build_label_for_stack(stack)])

stack_name = env.get('stack_name')
gitref = env.get('gitref')
neptune_ref = env.get('neptune_ref')

set_build_display_for_deploy(gitref, stack, stack_name, neptune_ref)
