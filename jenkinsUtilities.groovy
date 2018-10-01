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

def abort_unless_valid_stack_config(build, stack, stack_name) {
  // Abort the build if no stack set
  if (stack == '' && stack_name == '') {
    error = 'No stack specified.'
  } else if (stack != '' && stack_name != '') {
    error = 'Name for Softlayer, stack, and OpenStack, stack_name, provided.'
  } else {
    error = null
  }

  if (error != null) {
    error_message = 'ERROR: ' + error
    build.displayName = error_message
    println(error_message)
    throw new InterruptedException(error_message)
  }
}

def set_build_display_for_stack(stack, stack_name, neptune_ref) {
  build.displayName = "${neptune_ref}>${stack}${stack_name}"
}
