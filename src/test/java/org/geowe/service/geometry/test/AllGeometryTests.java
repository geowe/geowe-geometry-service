package org.geowe.service.geometry.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BufferTest.class, IntersectionTest.class, UnionTest.class,
	CentroidTest.class, EnvelopeTest.class,	DifferenceTest.class,
	SymDifferenceTest.class, IntersectTest.class, UnionTest.class, DivisionTest.class })
public class AllGeometryTests {

}
