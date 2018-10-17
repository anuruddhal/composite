/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.composite;

import org.ballerinalang.compiler.plugins.AbstractCompilerPlugin;
import org.ballerinalang.compiler.plugins.SupportedAnnotationPackages;
import org.ballerinalang.model.elements.PackageID;
import org.ballerinalang.model.tree.AnnotationAttachmentNode;
import org.ballerinalang.model.tree.PackageNode;
import org.ballerinalang.model.tree.VariableNode;
import org.ballerinalang.util.diagnostic.DiagnosticLog;
import org.wso2.ballerinalang.compiler.tree.BLangVariable;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangLiteral;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangRecordLiteral;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.wso2.composite.utils.DockerGenUtils.printError;

/**
 * Compiler plugin to generate docker artifacts.
 */
@SupportedAnnotationPackages(
        value = "wso2/composite:0.0.0"
)
public class DockerPlugin extends AbstractCompilerPlugin {

    @Override
    public void init(DiagnosticLog diagnosticLog) {
    }

    @Override
    public void process(PackageNode packageNode) {
    }

    @Override
    public void process(VariableNode recordNode, List<AnnotationAttachmentNode> annotations) {
        String dockerFilePath = (((BLangLiteral) ((BLangRecordLiteral.BLangRecordKeyValue) ((ArrayList) (
                (BLangRecordLiteral) ((BLangRecordLiteral.BLangRecordKeyValue) ((ArrayList) ((BLangRecordLiteral) (
                        (BLangVariable) recordNode).expr).keyValuePairs).get(1)).valueExpr).keyValuePairs).get(0))
                .valueExpr).value).toString();
        String tag = (((BLangLiteral) ((BLangRecordLiteral.BLangRecordKeyValue) ((ArrayList) (
                (BLangRecordLiteral) ((BLangRecordLiteral.BLangRecordKeyValue) ((ArrayList) ((BLangRecordLiteral) (
                        (BLangVariable) recordNode).expr).keyValuePairs).get(1)).valueExpr).keyValuePairs).get(1))
                .valueExpr).value).toString();
        printError(dockerFilePath + ":" + tag);
        DockerImageBuilder imageBuilder = new DockerImageBuilder();
        try {
            imageBuilder.buildImage(dockerFilePath, tag);
        } catch (IOException e) {
            printError(e.getMessage());
        } catch (InterruptedException e) {
            printError(e.getMessage());
        }
    }


    @Override
    public void codeGenerated(PackageID packageID, Path binaryPath) {
    }

}
