package com.github.xpenatan.gdx.backends.web;

import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.InputStream;

import com.badlogic.gdx.files.FileHandle;

public interface WebPreLoader {

	public InputStream read (String url);

	public String getFileText(String file);

	public boolean isText (String url);

	public FileHandle[] list (String url);

	public FileHandle[] list (String url, FileFilter filter);

	public FileHandle[] list (String url, FilenameFilter filter);

	public FileHandle[] list (String url, String suffix);

	public boolean isDirectory (String url);

	public boolean contains (String url);

	public long length (String url);
}
