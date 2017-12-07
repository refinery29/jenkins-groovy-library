import groovy.json.JsonSlurper

// Look for GitHub webhook payload in configured var or default to 'payload'
try {
    assert githubHookPayloadVar in String
} catch(e) {
    githubHookPayloadVar = "payload"
}

// Look for confiured GitHub webhook valid actions
try {
    /* This is somewhat naive as it only asserts that the variable is a List,
     * not that the List only contains Strings, and that those Strings are
     * a subset of the list at
     * https://developer.github.com/v3/activity/events/types/#pullrequestevent
     */
    assert githubHookJobActions in List
} catch(e) {
    githubHookJobActions = ["synchronize", "opened"]
}

// Parse object from json payload for current build
def currentBuildJSON = new JsonSlurper().parseText(build.buildVariables.get(githubHookPayloadVar))

// Confirm "action" is a key in currentBuildJSON and check if its value is in the defined list
if (!("action" in currentBuildJSON && currentBuildJSON["action"] in githubHookJobActions)) {
    build.delete()
}

null
