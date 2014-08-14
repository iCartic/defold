package com.dynamo.bob.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.dynamo.bob.LibraryException;

public class LibraryUtil {

    /** Convert the supplied URL into the corresponding filename on disk.
     * 
     * @param url Url of the library
     * @return the corresponding filename of the library on disk
     */
    public static String libUrlToFilename(URL url) {
        String fileName = url.toString().replaceAll("[/:\\.]", "_");
        return fileName + ".zip";
    }

    /** Convert a list of library URLs into a list of corresponding files on disk.
     * 
     * @param libPath base path of the library files
     * @param libUrls list of library URLs to convert
     * @return a list of corresponding files on disk
     */
    public static List<File> convertLibraryUrlsToFiles(String libPath, List<URL> libUrls) {
        List<File> files = new ArrayList<File>();
        for (URL url : libUrls) {
            files.add(new File(FilenameUtils.concat(libPath, libUrlToFilename(url))));
        }
        return files;
    }

    /**
     * Fetch the include dirs from the game.project file embedded in the specified archive.
     * The game.project is assumed to contain a comma separated list under the key library.include_dirs.
     * 
     * @param archive archive in which to search for the game.project file
     * @return a set of include dir names
     * @throws IOException
     * @throws ParseException
     */
    public static Set<String> readIncludeDirsFromArchive(ZipFile archive) throws IOException, ParseException {
        Set<String> includeDirs = new HashSet<String>();
        ZipEntry projectEntry = archive.getEntry("game.project");
        if (projectEntry != null) {
            InputStream is = null;
            try {
                is = archive.getInputStream(projectEntry);
                BobProjectProperties properties = new BobProjectProperties();
                properties.load(is);
                String dirs = properties.getStringValue("library", "include_dirs", "");
                for (String dir : dirs.split("[,\\s]")) {
                    if (!dir.isEmpty()) {
                        includeDirs.add(dir);
                    }
                }
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
        return includeDirs;
    }

    /**
     * Parse a comma separated string of URLs.
     * @param urls
     * @return a list of the parsed URLs
     */
    public static List<URL> parseLibraryUrls(String urls) throws LibraryException {
        List<URL> result = new ArrayList<URL>();
        String[] libUrls = urls.split("[,\\s]");
        for (String urlStr : libUrls) {
            urlStr = urlStr.trim();
            if (!urlStr.isEmpty()) {
                try {
                    URL url = new URL(urlStr);
                    result.add(url);
                } catch (MalformedURLException e) {
                    throw new LibraryException(String.format("The library URL %s is not valid", urlStr), e);
                }
            }
        }
        return result;
    }

    public static List<URL> getLibraryUrlsFromProject(String rootPath) throws LibraryException {
        rootPath = FilenameUtils.normalizeNoEndSeparator(rootPath, true);
        List<URL> urls = new ArrayList<URL>();
        BobProjectProperties properties = new BobProjectProperties();
        File projectProps = new File(FilenameUtils.concat(rootPath, "game.project"));
        if (!projectProps.exists()) {
            // Silently ignore if game.project does not exist, probably a test
            return urls;
        }
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(projectProps));
            properties.load(input);
        } catch (Exception e) {
            throw new LibraryException("Failed to parse game.project", e);
        } finally {
            IOUtils.closeQuietly(input);
        }
        String dependencies = properties.getStringValue("project", "dependencies", null);
        if (dependencies != null) {
            urls = parseLibraryUrls(dependencies);
        }
        return urls;
    }
}
