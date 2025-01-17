package com.github.xpenatan.gdx.backends.dragome;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.XMLHttpRequest;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLCanvasElement;
import org.w3c.dom.typedarray.ArrayBuffer;
import org.w3c.dom.typedarray.ArrayBufferView;
import org.w3c.dom.typedarray.Float32Array;
import org.w3c.dom.typedarray.Float64Array;
import org.w3c.dom.typedarray.Int16Array;
import org.w3c.dom.typedarray.Int32Array;
import org.w3c.dom.typedarray.Int8Array;
import org.w3c.dom.typedarray.Uint16Array;
import org.w3c.dom.typedarray.Uint32Array;
import org.w3c.dom.typedarray.Uint8Array;
import org.w3c.dom.typedarray.Uint8ClampedArray;
import org.w3c.dom.websocket.WebSocket;
import com.dragome.commons.ChainedInstrumentationDragomeConfigurator;
import com.dragome.commons.DragomeConfiguratorImplementor;
import com.dragome.commons.compiler.PrioritySolver;
import com.dragome.commons.compiler.annotations.CompilerType;
import com.dragome.commons.compiler.classpath.Classpath;
import com.dragome.commons.compiler.classpath.ClasspathEntry;
import com.dragome.commons.compiler.classpath.ClasspathFile;
import com.dragome.commons.compiler.classpath.InMemoryClasspathFile;
import com.dragome.commons.compiler.classpath.serverside.VirtualFolderClasspathEntry;
import com.dragome.web.config.DomHandlerDelegateStrategy;
import com.dragome.web.enhancers.jsdelegate.JsCast;
import com.dragome.web.enhancers.jsdelegate.interfaces.SubTypeFactory;
import com.dragome.web.enhancers.jsdelegate.serverside.JsDelegateGenerator;
import com.dragome.web.helpers.DefaultClasspathFileFilter;
import com.dragome.web.html.dom.w3c.ArrayBufferFactory;
import com.dragome.web.html.dom.w3c.TypedArraysFactory;
import com.dragome.web.services.RequestExecutorImpl.XMLHttpRequestExtension;
import com.github.xpenatan.gdx.backends.web.dom.CanvasPixelArrayWrapper;
import com.github.xpenatan.gdx.backends.web.dom.DocumentWrapper;
import com.github.xpenatan.gdx.backends.web.dom.ElementWrapper;
import com.github.xpenatan.gdx.backends.web.dom.EventHandlerWrapper;
import com.github.xpenatan.gdx.backends.web.dom.EventListenerWrapper;
import com.github.xpenatan.gdx.backends.web.dom.EventTargetWrapper;
import com.github.xpenatan.gdx.backends.web.dom.EventWrapper;
import com.github.xpenatan.gdx.backends.web.dom.HTMLCanvasElementWrapper;
import com.github.xpenatan.gdx.backends.web.dom.HTMLDocumentWrapper;
import com.github.xpenatan.gdx.backends.web.dom.HTMLElementWrapper;
import com.github.xpenatan.gdx.backends.web.dom.HTMLImageElementWrapper;
import com.github.xpenatan.gdx.backends.web.dom.HTMLVideoElementWrapper;
import com.github.xpenatan.gdx.backends.web.dom.ImageDataWrapper;
import com.github.xpenatan.gdx.backends.web.dom.KeyboardEventWrapper;
import com.github.xpenatan.gdx.backends.web.dom.LocationWrapper;
import com.github.xpenatan.gdx.backends.web.dom.MouseEventWrapper;
import com.github.xpenatan.gdx.backends.web.dom.NodeWrapper;
import com.github.xpenatan.gdx.backends.web.dom.TouchEventWrapper;
import com.github.xpenatan.gdx.backends.web.dom.TouchListWrapper;
import com.github.xpenatan.gdx.backends.web.dom.TouchWrapper;
import com.github.xpenatan.gdx.backends.web.dom.WheelEventWrapper;
import com.github.xpenatan.gdx.backends.web.dom.WindowWrapper;
import com.github.xpenatan.gdx.backends.web.dom.XMLHttpRequestWrapper;
import com.github.xpenatan.gdx.backends.web.dom.typedarray.ArrayBufferViewWrapper;
import com.github.xpenatan.gdx.backends.web.dom.typedarray.ArrayBufferWrapper;
import com.github.xpenatan.gdx.backends.web.dom.typedarray.Float32ArrayWrapper;
import com.github.xpenatan.gdx.backends.web.dom.typedarray.Float64ArrayWrapper;
import com.github.xpenatan.gdx.backends.web.dom.typedarray.FloatArrayWrapper;
import com.github.xpenatan.gdx.backends.web.dom.typedarray.Int16ArrayWrapper;
import com.github.xpenatan.gdx.backends.web.dom.typedarray.Int32ArrayWrapper;
import com.github.xpenatan.gdx.backends.web.dom.typedarray.Int8ArrayWrapper;
import com.github.xpenatan.gdx.backends.web.dom.typedarray.LongArrayWrapper;
import com.github.xpenatan.gdx.backends.web.dom.typedarray.ObjectArrayWrapper;
import com.github.xpenatan.gdx.backends.web.dom.typedarray.Uint8ArrayWrapper;
import com.github.xpenatan.gdx.backends.web.gl.WebGLActiveInfoWrapper;
import com.github.xpenatan.gdx.backends.web.gl.WebGLBufferWrapper;
import com.github.xpenatan.gdx.backends.web.gl.WebGLContextAttributesWrapper;
import com.github.xpenatan.gdx.backends.web.gl.WebGLFramebufferWrapper;
import com.github.xpenatan.gdx.backends.web.gl.WebGLProgramWrapper;
import com.github.xpenatan.gdx.backends.web.gl.WebGLRenderbufferWrapper;
import com.github.xpenatan.gdx.backends.web.gl.WebGLRenderingContextWrapper;
import com.github.xpenatan.gdx.backends.web.gl.WebGLShaderWrapper;
import com.github.xpenatan.gdx.backends.web.gl.WebGLTextureWrapper;
import com.github.xpenatan.gdx.backends.web.gl.WebGLUniformLocationWrapper;

/**
 * @author xpenatan
 */
@DragomeConfiguratorImplementor(priority= 10)
public class DragomeGdxConfigurator extends ChainedInstrumentationDragomeConfigurator
{

	private static Logger LOGGER= Logger.getLogger(DragomeGdxConfigurator.class.getName());
	final StringBuilder sb = new StringBuilder();

	String projName;

	DragomeBuildConfiguration configurator;

	protected JsDelegateGenerator jsDelegateGenerator;
	protected List<Class<?>> classes= new ArrayList<>(Arrays.asList(
			WebGLActiveInfoWrapper.class, WebGLBufferWrapper.class, WebGLContextAttributesWrapper.class, WebGLFramebufferWrapper.class, WebGLProgramWrapper.class,
			WebGLRenderbufferWrapper.class, WebGLRenderingContextWrapper.class, WebGLShaderWrapper.class, WebGLTextureWrapper.class, WebGLUniformLocationWrapper.class,
			CanvasPixelArrayWrapper.class, DocumentWrapper.class, ElementWrapper.class, HTMLCanvasElementWrapper.class, HTMLDocumentWrapper.class, HTMLElementWrapper.class,
			HTMLVideoElementWrapper.class, ImageDataWrapper.class, HTMLImageElementWrapper.class, WindowWrapper.class,
			ArrayBufferViewWrapper.class, ArrayBufferWrapper.class, FloatArrayWrapper.class,

			Float32ArrayWrapper.class, Int32ArrayWrapper.class, Int16ArrayWrapper.class, Uint8ArrayWrapper.class, Float64ArrayWrapper.class, Int8ArrayWrapper.class,

			LongArrayWrapper.class, ObjectArrayWrapper.class,
			Element.class, Document.class, HTMLCanvasElement.class, EventTargetWrapper.class, EventWrapper.class, EventListenerWrapper.class,
			ArrayBuffer.class, ArrayBufferView.class, Float32Array.class, Float64Array.class, Int16Array.class,
			Int32Array.class, Int8Array.class, Uint8ClampedArray.class, Uint16Array.class, Uint32Array.class, Uint8Array.class,
			ArrayBufferFactory.class, TypedArraysFactory.class, EventTarget.class, EventListener.class, Event.class,
			WheelEventWrapper.class, TouchWrapper.class, TouchListWrapper.class, TouchEventWrapper.class, NodeWrapper.class, KeyboardEventWrapper.class, MouseEventWrapper.class,
			XMLHttpRequest.class, Object.class, WebSocket.class, XMLHttpRequestExtension.class, LocationWrapper.class, XMLHttpRequestWrapper.class, EventHandlerWrapper.class
			));

	public DragomeGdxConfigurator(DragomeBuildConfiguration configurator)
	{
		super();
		this.configurator = configurator;
		String projPath= System.getProperty("user.dir");
		File file= new File(projPath);
		projName = file.getName().replace("\\", "/");

		final String thisClassName = getClass().getSimpleName();

		setClasspathFilter(new DefaultClasspathFileFilter()
		{
			@Override
			public boolean accept(ClasspathFile classpathFile)
			{
				if(!super.accept(classpathFile))
					return false;
				boolean flag = true;
				String fileName = classpathFile.getFilename();
				String path = classpathFile.getPath();
				path = path.replace("\\", "/");

				{
					flag = true;

					// All classes will compile except
//					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "java/util/function/BiConsumer", flag);
//					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "java/util/function/BiFunction", flag);
//					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "java/util/function/BinaryOperator", flag);
//					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "java/util/function/BiPredicate", flag);
//					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "java/util/function/Consumer", flag);
//					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "java/util/function/Function", flag);
//					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "java/util/function/Predicate", flag);
//					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "java/util/PrimitiveIterator", flag);
//					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "java/util/stream/Collectors", flag);
//					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "java/util/StreamImpl", flag);


					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, DragomeGdxConfigurator.class.getSimpleName(), flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, DragomeStandaloneAppGenerator.class.getSimpleName(), flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/github/xpenatan/gdx/backends/dragome/preloader/AssetFilter", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/github/xpenatan/gdx/backends/dragome/preloader/AssetsCopy", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/github/xpenatan/gdx/backends/dragome/preloader/DefaultAssetFilter", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/github/xpenatan/gdx/backends/dragome/preloader/DefaultAssetFilter", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".conf", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "javax/", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "junit/", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/junit/", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "sun/reflect", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".html", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".idl", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".css", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".MF", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".xml", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".txt", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".properties", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".template", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/dragome/tests/", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/dragome/web/config/DomHandlerApplicationConfigurator", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/dragome/web/helpers/", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/dragome/commons/", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/dragome/compiler/", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/android/", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/jf", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/xmlvm/", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/github/javaparser/", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/hamcrest/", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/badlogic/gdx/jnigen/", flag);
					flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, thisClassName, flag);

					{
						//Ignore classes added by gradle getty. Eclipse jetty is not needed.
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "javolution/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "net/jpountz/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "net/sf/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".tld", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "proguard/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "com/google/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "io/netty/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "JDOMAbout", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "ro/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/apache", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/xml/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/mockito/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/atmosphere/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/objectweb/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/objenesis/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/hamcrest/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/dom4j/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/slf4j/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/jdom/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "org/reflections/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "META-INF/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "javassist/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".vm", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".so", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, ".dylib", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "/serverside/", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "LICENSE", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "NOTICE", flag);
						flag = toAccept(ACCEPT_TYPE.DONT_ACCEPT, path, "Main.class", flag);
					}

					flag = toAccept(ACCEPT_TYPE.ACCEPT, path, "com/dragome/commons/AbstractProxyRelatedInvocationHandler", flag);
					flag = toAccept(ACCEPT_TYPE.ACCEPT, path, "com/dragome/commons/ProxyRelatedInvocationHandler", flag);
					flag = toAccept(ACCEPT_TYPE.ACCEPT, path, "com/dragome/commons/compiler/annotations/", flag);
					flag = toAccept(ACCEPT_TYPE.ACCEPT, path, "com/dragome/commons/javascript/", flag);
				}

				if(path.endsWith("/"))
					flag = false;

				if(flag)
					System.out.println("Allow Class: " + flag + " Path: " + path);

				return flag;
			}
		});

	}

	@Override
	public boolean isRemoveUnusedCode()
	{
		return false;
	}

	@Override
	public boolean isObfuscateCode()
	{
		return false;
	}

	@Override
	public List<URL> getAdditionalCodeKeepConfigFile()
	{
		URL resource = getClass().getResource("/dragome/proguard-extra.conf");
		return Arrays.asList(resource);
	}

	@Override
	public List<URL> getAdditionalObfuscateCodeKeepConfigFile()
	{
		URL resource = getClass().getResource("/proguard-extra.conf");
		return Arrays.asList(resource);
	}

	@Override
	public boolean filterClassPath(String classpathEntry)
	{
		boolean include= super.filterClassPath(classpathEntry);
		classpathEntry = classpathEntry.replace("\\", "/");
		include |= classpathEntry.contains(projName + "/") && classpathEntry.endsWith("/bin");
		include |= classpathEntry.contains("dragome-js-jre");
		include |= classpathEntry.contains("dragome-web");
		include |= classpathEntry.contains("dragome-core");
		include |= classpathEntry.contains("dragome-js-commons");
		include |= classpathEntry.contains("dragome-w3c-standards");
		include |= classpathEntry.contains("dragome-bytecode-js-compiler");

		include &= !classpathEntry.contains("/resources/");


		if(!include && classpathEntry.contains("/bin"))
			include = true;

		if(include) {
			String text = "Compile: " + include + " path: " + classpathEntry + "\n";

			System.out.println(text);
		}

		return include;
	}

	@Override
	public void sortClassPath(Classpath classPath) {
		classPath.sortByPriority(new PrioritySolver()
		{
			@Override
			public int getPriorityOf(ClasspathEntry string)
			{
				String name = string.getName();
				if (name.contains("backend-dragome"))
					return 5; // dragome backend is first so it can override any dragome classes
				else if (name.contains("backend-web"))
					return 4;
				else if (name.contains("dragome-js-jre"))
					return 3;
				else if (name.contains("dragome-"))
					return 2;
				else if (name.contains("gdx-box2d-gwt"))
					return 1;
				else
					return 0;
			}
		});

		if(sb.length() > 0)
			sb.insert(0, "\n" + "########### Libs/Classes PATH to allow Dragome to compile ###########" + "\n");

		Iterator<ClasspathEntry> iterator = classPath.getEntries().iterator();
		int i = 0;
		sb.append("\n" + "######################## Libs ClassPath Order ########################\n");
		while(iterator.hasNext()) {
			ClasspathEntry next = iterator.next();
			String name = next.getName();
			if(next instanceof VirtualFolderClasspathEntry)
				name = "Delegate Virtual Path";
			sb.append(i + ": " + name);
			sb.append("\n");
			i++;
		}
		sb.append("#################################################################");
		LOGGER.info(sb.toString());
		sb.setLength(0);
	}

	@Override
	public List<ClasspathEntry> getExtraClasspath(Classpath classpath)
	{
		List<ClasspathEntry> result= new ArrayList<ClasspathEntry>();
		List<ClasspathFile> classpathFiles= new ArrayList<ClasspathFile>();

		result.add(new VirtualFolderClasspathEntry(classpathFiles));

		if (jsDelegateGenerator == null)
			createJsDelegateGenerator(classpath);

		for (Class<?> class1 : classes)
		{
			InMemoryClasspathFile inMemoryClasspathFile= jsDelegateGenerator.generateAsClasspathFile(class1);
			addClassBytecode(inMemoryClasspathFile.getBytecode(), inMemoryClasspathFile.getClassname());
			classpathFiles.add(inMemoryClasspathFile);
		}

		return result;
	}

	private void createJsDelegateGenerator(Classpath classpath)
	{
		jsDelegateGenerator= new JsDelegateGenerator(classpath.toString().replace(";", System.getProperty("path.separator")), new DomHandlerDelegateStrategy()
		{
			@Override
			public String createMethodCall(Method method, String params) {
				String longName = method.toGenericString();
				String name = method.getName();
				Class<?> declaringClass = method.getDeclaringClass();

				if(declaringClass == HTMLDocumentWrapper.class && name.startsWith("createElement"))
				{
					System.out.println("");
				}

				String result = super.createMethodCall(method, params);

				if(name.startsWith("get")) {
					String tmpName = null;
					if(name.endsWith("Float"))
						tmpName = name.replace("Float", "");
					else if(name.endsWith("Int"))
						tmpName = name.replace("Int", "");
					else if(name.endsWith("String"))
						tmpName = name.replace("String", "");
					else if(name.endsWith("Boolean"))
						tmpName = name.replace("Boolean", "");
					if(tmpName != null) {
						if(params != null) {
							result = "this.node." + tmpName + "(" + params + ")";
						}
						else {
							result = "this.node." + tmpName;
						}
					}
				}

				if(declaringClass == EventTargetWrapper.class && name.startsWith("addEventListener")) {
					if(method.getParameterTypes().length >= 2) {
						String castEventToDelegate = "var newEvent = com_dragome_web_enhancers_jsdelegate_JsCast.$castTo___java_lang_Object__java_lang_Class$java_lang_Object(event, java_lang_Class.$forName___java_lang_String$java_lang_Class('com.github.xpenatan.gdx.backend.web.dom.EventWrapper'));";
						String callMethod = "$2.$handleEvent___com_github_xpenatan_gdx_backend_web_dom_EventWrapper$void(newEvent);";
						String paramListener = "var listener = function myFunction(event) {"+ castEventToDelegate + callMethod  +"};";
						String param = "$1, listener";
						if(method.getParameterTypes().length == 3)
							param += ",$3";
						result = paramListener + " this.node.addEventListener(" + param + ");";
					}
				}

				if(declaringClass == HTMLImageElementWrapper.class && name.startsWith("addEventListener")) {
					if(method.getParameterTypes().length >= 2) {
						String castEventToDelegate = "var newEvent = com_dragome_web_enhancers_jsdelegate_JsCast.$castTo___java_lang_Object__java_lang_Class$java_lang_Object(event, java_lang_Class.$forName___java_lang_String$java_lang_Class('com.github.xpenatan.gdx.backend.web.dom.EventWrapper'));";
						String callMethod = "$2.$handleEvent___com_github_xpenatan_gdx_backend_web_dom_EventWrapper$void(newEvent);";
						String paramListener = "var listener = function myFunction(event) {"+ castEventToDelegate + callMethod  +"};";
						String param = "$1, listener";
						if(method.getParameterTypes().length == 3)
							param += ",$3";
						result = paramListener + " this.node.addEventListener(" + param + ");";
					}
				}
				if(declaringClass == XMLHttpRequestWrapper.class && name.startsWith("setOnreadystatechange")) {
					if(method.getParameterTypes().length == 1) {
						String castEventToDelegate = "var newEvent = com_dragome_web_enhancers_jsdelegate_JsCast.$castTo___java_lang_Object__java_lang_Class$java_lang_Object(event, java_lang_Class.$forName___java_lang_String$java_lang_Class('com.github.xpenatan.gdx.backend.web.dom.EventWrapper'));";
						String callMethod = "$1.$handleEvent___com_github_xpenatan_gdx_backend_web_dom_EventWrapper$void(newEvent);";
						String paramListener = "var listener = function myFunction(event) {"+ castEventToDelegate + callMethod  +"};";
						String param = "listener";
						result = paramListener + " this.node.onreadystatechange = " + param + ";";
					}
				}

				Class<?>[] superclass= method.getDeclaringClass().getInterfaces();
				boolean isTypedArray= superclass.length > 0 && superclass[0].equals(ArrayBufferViewWrapper.class);

				if (isTypedArray && name.equals("set") && method.getParameterTypes().length == 2 && method.getParameterTypes()[0].equals(int.class))
					result= "this.node[$1] = $2";
				else if (isTypedArray && (name.equals("get") || name.equals("getAsDouble")) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(int.class))
					result= "this.node[$1]";

				return result;
			}

			@Override
			public String getSubTypeExtractorFor(Class<?> interface1, String methodName) {
				String subTypeExtractorFor = super.getSubTypeExtractorFor(interface1, methodName);
				return subTypeExtractorFor;
			}

			@Override
			public Class<? extends SubTypeFactory> getSubTypeFactoryClassFor(Class<?> interface1, String methodName) {
				Class<? extends SubTypeFactory> subTypeFactoryClassFor = super.getSubTypeFactoryClassFor(interface1, methodName);
				return subTypeFactoryClassFor;
			}
		});
	}

	@Override
	public boolean isCaching()
	{
		return false;
	}

	public boolean toAccept(ACCEPT_TYPE filter, String path, String toSearch, boolean flag) {
		boolean contains = path.contains(toSearch);
		if(filter == ACCEPT_TYPE.DONT_ACCEPT)
			flag &= !contains;
		else if(filter == ACCEPT_TYPE.ACCEPT)
			flag |= contains;
		return flag;
	}

	enum ACCEPT_TYPE{
		ACCEPT, DONT_ACCEPT
	}


//	@Override
//	public String mainClassName() {
//		return configurator.getMainClass().getName();
//	}

	@Override
	public CompilerType getDefaultCompilerType() {
		return CompilerType.Standard;
	}

	@Override
	public boolean isCheckingCast() {
		return true;
	}
}
