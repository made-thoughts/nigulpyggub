package club.devcord.gamejam.world;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class WorldDuplicator {
    private final Nigulpyggub plugin;

    private final Path defaultWorld = Path.of("world");

    public WorldDuplicator(Nigulpyggub plugin) {
        this.plugin = plugin;
    }

    public World duplicate(String name) {
        String worldName = "world_" + name;
        Path worldPath = Path.of(worldName);
        var existingWorld = Bukkit.getWorld(worldName);
        if(existingWorld != null) {
            Bukkit.unloadWorld(existingWorld, false);
        }
        recursiveDelete(worldPath);
        recursiveCopy(defaultWorld, worldPath);
        return Bukkit.createWorld(WorldCreator.name(worldName));
    }

    private void recursiveCopy(Path from, Path to) {
        try {
            Files.walk(from)
                    .forEach(path -> {
                        if (Files.isDirectory(path) || path.getFileName().equals(Path.of("uid.dat"))) return;
                        try {
                            Path destination = to.resolve(path.subpath(from.getNameCount(), path.getNameCount()));
                            Files.createDirectories(destination.getParent());
                            Files.copy(path, destination);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void recursiveDelete(Path path) {
        try {
            Files.walkFileTree(path,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult postVisitDirectory(
                                Path dir, IOException exc) throws IOException {
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFile(
                                Path file, BasicFileAttributes attrs)
                                throws IOException {
                            Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
