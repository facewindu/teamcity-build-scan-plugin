package nu.studer.teamcity.buildscan.internal;

import jetbrains.buildServer.messages.BuildMessage1;
import jetbrains.buildServer.messages.serviceMessages.ServiceMessage;
import jetbrains.buildServer.messages.serviceMessages.ServiceMessageTranslator;
import jetbrains.buildServer.serverSide.SRunningBuild;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

final class BuildScanServiceMessageListener implements ServiceMessageTranslator {

    // values need to be kept in sync with build-scan-init.gradle
    private static final String BUILD_SCAN_SERVICE_MESSAGE_NAME = "buildscan";
    private static final String BUILD_STARTED_MESSAGE = "BUILD_STARTED";

    private final BuildScanDataStore buildScanDataStore;

    BuildScanServiceMessageListener(@NotNull BuildScanDataStore buildScanDataStore) {
        this.buildScanDataStore = buildScanDataStore;
    }

    @NotNull
    @Override
    public List<BuildMessage1> translate(@NotNull SRunningBuild runningBuild, @NotNull BuildMessage1 originalMessage, @NotNull ServiceMessage serviceMessage) {
        String argument = requireNonNull(serviceMessage.getArgument());
        if (argument.equals(BUILD_STARTED_MESSAGE)) {
            buildScanDataStore.mark(runningBuild);
        } else {
            buildScanDataStore.store(runningBuild, argument);
        }

        // omit these service messages from the final build log
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public String getServiceMessageName() {
        return BUILD_SCAN_SERVICE_MESSAGE_NAME;
    }

}