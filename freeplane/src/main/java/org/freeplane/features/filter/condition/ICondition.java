/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2010 Dimitry Polivaev, Volker Boerchers
 *
 *  This file authors are Dimitry Polivaev, Volker Boerchers
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 */
package org.freeplane.features.filter.condition;

import org.freeplane.features.map.NodeModel;

public interface ICondition {
	boolean checkNode(NodeModel node);

    default boolean checksParent() {
        return false;
    }

    default boolean checksAncestors() {
        return false;
    }

    default boolean checksChildren() {
        return false;
    }

    default boolean checksDescendants() {
        return false;
    }
}
