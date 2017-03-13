package org.geowe.service.geometry.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;

/**
 * LineNoder representa la entidad responsable de nodar las líneas/segmentos que forman una geometría.
 * 
 * @author jose@geowe.org
 *
 */
public class LineNoder {

	public Collection<Geometry> nodeLines(final Collection<Geometry> lines) {
		final GeometryFactory gf = new GeometryFactory();
		final Geometry linesGeom = gf.createMultiLineString(GeometryFactory
				.toLineStringArray(lines));
		Geometry unionInput = gf.createMultiLineString(null);
		final Geometry point = extractPoint(lines);
		if (point != null)
			unionInput = point;

		final Geometry noded = linesGeom.union(unionInput);
		final List<Geometry> nodedList = new ArrayList<Geometry>();
		nodedList.add(noded);
		return nodedList;
	}

	public Geometry extractPoint(final Collection<Geometry> lines) {
		Geometry point = null;
		for (final Iterator<Geometry> i = lines.iterator(); i.hasNext();) {
			final Geometry geometry = i.next();
			if (!geometry.isEmpty()) {
				point = geometry.getFactory().createPoint(geometry.getCoordinate());
			}
		}
		return point;
	}
	
	protected Collection<Geometry> polygonizer(final Collection<Geometry> nodedLines) {
		final Polygonizer polygonizer = new Polygonizer();		
		polygonizer.add(nodedLines);
		return polygonizer.getPolygons();
	}
}
