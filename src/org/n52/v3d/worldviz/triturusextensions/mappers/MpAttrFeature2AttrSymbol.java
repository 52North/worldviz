package org.n52.v3d.worldviz.triturusextensions.mappers;

import org.n52.v3d.triturus.core.T3dProcMapper;
import org.n52.v3d.triturus.t3dutil.T3dSymbolDef;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dBox;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dCone;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dCube;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dCylinder;
import org.n52.v3d.triturus.t3dutil.symboldefs.T3dSphere;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgPoint;

/**
 * This mapper shall be used to create {@link T3dAttrSymbolInstance}-objects
 * from any {@link VgAttrFeature}.
 * 
 * @author Christian Danowski
 * 
 */
public class MpAttrFeature2AttrSymbol extends T3dProcMapper {

	// FIXME sollen sämtliche Attribute der VgAttrFeatures übernommen werden,
	// oder nur die, die dann tatsächlich für die visualle Ausgestaltung des
	// Symbols verwendet werden?

	/*
	 * Note that all attribute-value-combinations of the feature will be
	 * transferred to the returned symbolInstance.
	 */

	/**
	 * Creates a {@link T3dAttrSymbolInstance} by using a sphere with radius=1
	 * as symbol. Note that you can change the size of the symbol afterwards by
	 * using the <code>scale</code>-methods of {@link T3dAttrSymbolInstance}
	 * (e.g. {@link T3dAttrSymbolInstance#setyScale(double)})
	 * 
	 * @param feature
	 *            the attributed feature
	 * @param position
	 *            the position in the final scene at which the created
	 *            symbolInstance shall be placed
	 * @return
	 */
	public T3dAttrSymbolInstance createSphereSymbol(VgAttrFeature feature,
			VgPoint position) {

		T3dSphere sphere = new T3dSphere();
		sphere.setRadius(1);

		return createAttrSymbolInstance(feature, position, sphere);

	}

	/**
	 * Creates a {@link T3dAttrSymbolInstance} by using a cube with size=1 as
	 * symbol. Note that you can change the size of the symbol afterwards by
	 * using the <code>scale</code>-methods of {@link T3dAttrSymbolInstance}
	 * (e.g. {@link T3dAttrSymbolInstance#setyScale(double)})
	 * 
	 * @param feature
	 *            the attributed feature
	 * @param position
	 *            the position in the final scene at which the created
	 *            symbolInstance shall be placed
	 * @return
	 */
	public T3dAttrSymbolInstance createCubeSymbol(VgAttrFeature feature,
			VgPoint position) {
		T3dCube cube = new T3dCube();
		cube.setSize(1);

		return createAttrSymbolInstance(feature, position, cube);
	}

	/**
	 * Creates a {@link T3dAttrSymbolInstance} by using a box with size=(1,1,1)
	 * as symbol. Note that you can change the size of the symbol afterwards by
	 * using the <code>scale</code>-methods of {@link T3dAttrSymbolInstance}
	 * (e.g. {@link T3dAttrSymbolInstance#setyScale(double)})
	 * 
	 * @param feature
	 *            the attributed feature
	 * @param position
	 *            the position in the final scene at which the created
	 *            symbolInstance shall be placed
	 * @return
	 */
	public T3dAttrSymbolInstance createBoxSymbol(VgAttrFeature feature,
			VgPoint position) {
		T3dBox box = new T3dBox();
		box.setSize(1, 1, 1);

		return createAttrSymbolInstance(feature, position, box);
	}

	/**
	 * Creates a {@link T3dAttrSymbolInstance} by using a cone with radius=1 and
	 * height=1 as symbol. Note that you can change the size of the symbol
	 * afterwards by using the <code>scale</code>-methods of
	 * {@link T3dAttrSymbolInstance} (e.g.
	 * {@link T3dAttrSymbolInstance#setyScale(double)})
	 * 
	 * @param feature
	 *            the attributed feature
	 * @param position
	 *            the position in the final scene at which the created
	 *            symbolInstance shall be placed
	 * @return
	 */
	public T3dAttrSymbolInstance createConeSymbol(VgAttrFeature feature,
			VgPoint position) {
		T3dCone cone = new T3dCone();
		cone.setRadius(1);
		cone.setHeight(1);

		return createAttrSymbolInstance(feature, position, cone);
	}

	/**
	 * Creates a {@link T3dAttrSymbolInstance} by using a cylinder with radius=1
	 * and height=1 as symbol. Note that you can change the size of the symbol
	 * afterwards by using the <code>scale</code>-methods of
	 * {@link T3dAttrSymbolInstance} (e.g.
	 * {@link T3dAttrSymbolInstance#setyScale(double)})
	 * 
	 * @param feature
	 *            the attributed feature
	 * @param position
	 *            the position in the final scene at which the created
	 *            symbolInstance shall be placed
	 * @return
	 */
	public T3dAttrSymbolInstance createCylinderSymbol(VgAttrFeature feature,
			VgPoint position) {
		T3dCylinder cylinder = new T3dCylinder();
		cylinder.setRadius(1);
		cylinder.setHeight(1);

		return createAttrSymbolInstance(feature, position, cylinder);
	}

	private T3dAttrSymbolInstance createAttrSymbolInstance(
			VgAttrFeature feature, VgPoint position, T3dSymbolDef symbol) {
		T3dAttrSymbolInstance attrSymbolInstance = new T3dAttrSymbolInstance(
				symbol, position);

		// TODO siehe oben, macht das hier Sinn? oder hier keine Attribute
		// hinzufügen, sondern erst bei einem zweiten Mapper wie
		// MpValue2ColoredSymbol?
		// String[] attributeNames = feature.getAttributeNames();
		//
		// for (String attrName : attributeNames) {
		// attrSymbolInstance.addAttributeValuePair(attrName,
		// feature.getAttributeValue(attrName));
		// }

		return attrSymbolInstance;
	}

	@Override
	public String log() {
		// TODO Auto-generated method stub
		return null;
	}

}
