package com.dynamo.cr.editor.fs.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import com.dynamo.cr.client.IBranchClient;
import com.dynamo.cr.client.RepositoryException;
import com.dynamo.cr.editor.fs.RepositoryFileSystemPlugin;

public class RepositoryFileOutputStream extends ByteArrayOutputStream {

    private IBranchClient client;
    private IPath path;

    public RepositoryFileOutputStream(IBranchClient client, IPath path,
            int options, IProgressMonitor monitor) throws CoreException {
        this.client = client;
        this.path = path;

        try {
            if ((options & EFS.APPEND) != 0) {
                byte[] data = client.getResourceData(path.toPortableString(), "");
                write(data);
            }
        } catch (Throwable e) {
            RepositoryFileSystemPlugin.throwCoreExceptionError(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            client.putResourceData(path.toPortableString(), toByteArray());
        } catch (RepositoryException e) {
            throw new IOException(e);
        }
    }
}