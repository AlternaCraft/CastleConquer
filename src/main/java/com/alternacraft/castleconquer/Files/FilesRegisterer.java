/*
 * Copyright (C) 2016 AlternaCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.alternacraft.castleconquer.Files;

import com.alternacraft.aclib.files.PluginFile;
import com.alternacraft.aclib.files.PluginFiles;

/**
 * This class registers the plugin files and manages them.
 *
 * @author AlternaCraft
 */
public final class FilesRegisterer {
    public enum Files {
        // <editor-fold defaultstate="collapsed" desc="Files">
        ;
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Internal stuff">
        public String id = null;
        public PluginFile plFile = null;

        Files(String id, PluginFile file) {
            this.id = id;
            this.plFile = file;
        }
        // </editor-fold>
    }

    public FilesRegisterer() {
        for (Files file : Files.values()) {
            PluginFiles.registerFile(
                    file.id,
                    file.plFile
            );
        }
    }

    public PluginFile getFile(Files file) {
        return file.plFile;
    }
}
