build = Thread.currentThread().executable
env = build.getEnvironment()
neptune_branch = env.get('neptune_branch')
virtualenv = env.get('virtualenv')

build.displayName = "${neptune_branch}:${virtualenv}"
