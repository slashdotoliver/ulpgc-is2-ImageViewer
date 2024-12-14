package imageviewer.architecture.control.io;

import imageviewer.architecture.model.Image;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

public class FolderImageLoader implements ImageLoader {

    public static class EmptyImageFolderException extends Exception {
        public EmptyImageFolderException(String message) {
            super(message);
        }
    }

    private final static Set<String> SUPPORTED_IMAGE_EXTENSIONS = Set.of(
            "jpeg", "jpg", "png", "gif", "bmp", "webp", "tiff"
    );

    private final File[] files;

    public FolderImageLoader(File folder) throws IOException, EmptyImageFolderException {
        files = imagesUnder(folder);
        if (files.length == 0)
            throw new EmptyImageFolderException("No images were found under '" + folder.getPath() + "'");
    }

    private static FileFilter ofSupportedImageType() {
        return file -> isReadable(file) && hasSupportedImageExtension(file);
    }

    private static boolean isReadable(File file) {
        return file.canRead() && file.isFile();
    }

    private static boolean hasSupportedImageExtension(File file) {
        return SUPPORTED_IMAGE_EXTENSIONS.stream()
                .anyMatch(supportedExtension -> file.getName()
                        .toLowerCase()
                        .endsWith('.' + supportedExtension)
                );
    }

    private static File[] imagesUnder(File folder) throws IOException {
        File[] files = folder.listFiles(ofSupportedImageType());
        if (files == null)
            throw new IOException(
                    "Pathname does not denote a directory, or an I/O error occurred.\nPathname:" + folder.getPath()
            );
        return files;
    }

    @Override
    public Image load() {
        return imageAt(0);
    }

    public Image imageAt(int index) {
        return new Image() {
            @Override
            public byte[] content() throws IOException {
                return Files.readAllBytes(currentFile().toPath());
            }

            @Override
            public Image next() {
                return imageAt(nextIndex());
            }

            @Override
            public Image previous() {
                return imageAt(previousIndex());
            }

            @Override
            public String name() {
                return currentFile().getName();
            }

            private int nextIndex() {
                return (index + 1) % files.length;
            }

            private int previousIndex() {
                return index > 0 ? index - 1 : files.length - 1;
            }

            private File currentFile() {
                return files[index];
            }
        };
    }
}
