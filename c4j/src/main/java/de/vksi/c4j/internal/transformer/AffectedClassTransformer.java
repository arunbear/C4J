package de.vksi.c4j.internal.transformer;

import static de.vksi.c4j.internal.util.TransformationHelper.addClassAnnotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;

import org.apache.log4j.Logger;

import de.vksi.c4j.internal.RootTransformer;
import de.vksi.c4j.internal.Transformed;
import de.vksi.c4j.internal.util.AffectedBehaviorLocator;
import de.vksi.c4j.internal.util.ContractRegistry.ContractInfo;
import de.vksi.c4j.internal.util.ContractRegistry.ContractMethod;
import de.vksi.c4j.internal.util.ListOrderedSet;

public class AffectedClassTransformer {
	private Logger logger = Logger.getLogger(getClass());
	private AffectedBehaviorLocator affectedBehaviorLocator = new AffectedBehaviorLocator();
	private AbstractAffectedClassTransformer[] transformers = new AbstractAffectedClassTransformer[] {
			// beware: PureTransformer has to run first!
			// InvariantTransformer has to run after DynamicConditionTransformer
			new PureTransformer(), new DynamicConditionTransformer(), new InvariantTransformer(),
			new StaticConditionTransformer() };

	public void transform(ListOrderedSet<CtClass> involvedClasses, ListOrderedSet<ContractInfo> contracts,
			CtClass affectedClass) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("transforming class " + affectedClass.getName());
		}
		Map<CtBehavior, List<ContractMethod>> contractMap = getContractMap(contracts, affectedClass);
		for (AbstractAffectedClassTransformer transformer : transformers) {
			transformer.transform(involvedClasses, contracts, affectedClass, contractMap);
		}
		addClassAnnotation(affectedClass, RootTransformer.INSTANCE.getPool().get(Transformed.class.getName()));
	}

	protected Map<CtBehavior, List<ContractMethod>> getContractMap(ListOrderedSet<ContractInfo> contracts,
			CtClass affectedClass) throws NotFoundException, CannotCompileException {
		Map<CtBehavior, List<ContractMethod>> contractMap = new HashMap<CtBehavior, List<ContractMethod>>();
		for (ContractInfo contractInfo : contracts) {
			for (ContractMethod contractMethod : contractInfo.getMethods()) {
				CtBehavior affectedBehavior = affectedBehaviorLocator.getAffectedBehavior(contractInfo, affectedClass,
						contractMethod.getMethod());
				if (affectedBehavior != null) {
					if (!contractMap.containsKey(affectedBehavior)) {
						contractMap.put(affectedBehavior, new ArrayList<ContractMethod>());
					}
					contractMap.get(affectedBehavior).add(contractMethod);
				}
			}
		}
		return contractMap;
	}
}
