package com.ibm.cics.cbmp;

/*-
 * #%L
 * CICS Bundle Maven Plugin
 * %%
 * Copyright (C) 2019 IBM Corp.
 * %%
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * #L%
 */

import java.io.File;
import java.io.IOException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Warbundle extends BundlePart {
	
	private static final String TYPE = BundleConstants.NS + "/WARBUNDLE";
	
	private String name;
	private String jvmserver;
	
	public Warbundle() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getJvmserver() {
		return jvmserver;
	}
	
	public void setJvmserver(String jvmserver) {
		this.jvmserver = jvmserver;
	}

	@Override
	public void collectContent(File workDir, Artifact a) throws MojoExecutionRuntimeException {
		if (name == null) name = a.getArtifactId() + "-" + a.getVersion();
		try {
			FileUtils.copyFile(a.getFile(), new File(workDir, name + "." + a.getType()));
		} catch (IOException e) {
			throw new MojoExecutionRuntimeException("Error copying artifact file", e);
		}
	}

	@Override
	public Define writeBundlePart(File workDir, Artifact a) throws MojoExecutionRuntimeException {
		if (name == null) name = a.getArtifactId() + "-" + a.getVersion();
	
		//write define
		if (jvmserver == null || "".equals(jvmserver)) throw new MojoExecutionRuntimeException("JVM server was not supplied");
		
		Document document = BuildCICSBundleMojo.DOCUMENT_BUILDER.newDocument();
		Element rootElement = document.createElement("warbundle");
		rootElement.setAttribute("symbolicname", a.getArtifactId());
		rootElement.setAttribute("jvmserver", jvmserver);
		document.appendChild(rootElement);
		
		String definePath = name + ".warbundle";
		File define = new File(workDir, definePath);
		
		try {
			BuildCICSBundleMojo.TRANSFORMER.transform(
				new DOMSource(document),
				new StreamResult(define)
			);
		} catch (TransformerException e) {
			throw new MojoExecutionRuntimeException("Error writing define", e);
		}
		
		return new Define(name, TYPE, definePath);
	}
	
	public static <T> Collector<T, ?, T> toSingleton() {
	    return Collectors.collectingAndThen(
            Collectors.toList(),
            list -> {
                if (list.size() != 1) {
                    throw new IllegalStateException();
                }
                return list.get(0);
            }
	    );
	}
	
}
