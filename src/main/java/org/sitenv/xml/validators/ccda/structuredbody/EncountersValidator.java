package org.sitenv.xml.validators.ccda.structuredbody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import org.apache.log4j.Logger;
import org.sitenv.xml.validators.ccda.CcdaCodeValidator;
import org.sitenv.xml.xpathvalidator.engine.MultipleXPathNodeValidator;
import org.sitenv.xml.xpathvalidator.engine.data.XPathValidatorResult;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EncountersValidator implements MultipleXPathNodeValidator {

	private static final Logger logger = Logger.getLogger(EncountersValidator.class);
	private static final String[] EXPRESSIONS = {
		"entry/encounter/code",
		"entry/encounter/performer/assignedEntity/code",
		"entry/encounter/participant/participantRole/code",
		"entry/encounter/entryRelationship/observation/code"
	};
	
	// this should be used with the template 2.16.840.1.113883.10.20.22.2.6.1
	public List<XPathValidatorResult> validateNode(String expression, XPath xpath, Node node, int nodeIndex, Map<String, String> params) {
	
		List<XPathValidatorResult> results = null;
		 
		try {
			for (int count = 0; count < EXPRESSIONS.length; count++)
			{
				String localExp = EXPRESSIONS[count];
				XPathExpression expCode = xpath.compile(localExp);
				
				NodeList nodes = (NodeList) expCode.evaluate(node, XPathConstants.NODESET);
				
				for (int i = 0; i < nodes.getLength(); i++) {
					Node codeNode = nodes.item(i);
					
					CcdaCodeValidator codeValidator = new CcdaCodeValidator();
					List<XPathValidatorResult> result = codeValidator.validateNode(expression, nodeIndex, expression + "[" + (nodeIndex + 1) + "]/" + localExp, xpath, codeNode, i, params);
					
					if (results == null)
					{
						results = new ArrayList<XPathValidatorResult>();
					}
					
					results.addAll(result);
				}
			}
			
		} catch (XPathExpressionException e) {
			logger.error(e);
		}
		
		logger.debug("EXECUTED THE Encounters Validator");
		
		return results;
	}

}
