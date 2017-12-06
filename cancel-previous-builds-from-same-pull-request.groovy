import groovy.json.JsonSlurper

import hudson.model.Result
import hudson.model.Hudson
import hudson.console.ModelHyperlinkNote


// Look for GitHub webhook payload in configured var or default to 'payload'
try {
    assert githubHookPayloadVar in String
} catch(e) {
    githubHookPayloadVar = "payload"
}

// Parse object from json payload for current build
def currentBuildJSON = new JsonSlurper().parseText(build.buildVariables.get(githubHookPayloadVar))

// Iterate through current project runs
build.getProject()._getRuns().each { _, run ->

    // Only consider runs which are still running which aren't the current run
    if (run.getResult().equals(null) && run!=build) {
        def exec = run.getExecutor()

        // Parse object from json payload for run build
        def runBuildJSON = new JsonSlurper().parseText(run.buildVariables.get("payload"))

        if (runBuildJSON["pull_request"]["head"]["label"] == currentBuildJSON["pull_request"]["head"]["label"]
           && runBuildJSON["pull_request"]["base"]["label"] == currentBuildJSON["pull_request"]["base"]["label"]) {
            exec.interrupt(Result.ABORTED)
            run.setDescription("${run.description ?:''}\n\nAborted because <a href=\"${Hudson.instance.getRootUrl() + build.getUrl()}\">${build}</a> ran against the same PR.")
            println "Aborted duplicate " + ModelHyperlinkNote.encodeTo(Hudson.instance.getRootUrl() + run.getUrl(), run.getFullDisplayName())
        }
    }
}

null
