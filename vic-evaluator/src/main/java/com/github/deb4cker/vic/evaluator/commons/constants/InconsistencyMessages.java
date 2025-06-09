package com.github.deb4cker.vic.evaluator.commons.constants;

public class InconsistencyMessages
{
	private static final String SHOULD_BE = "deveria ser %s.";
	private static final String NOT_PRESENT = "não está presente na classe \"%s\". A assinatura deve estar EXATAMENTE igual ao do diagrama.";
	private static final String INCORRECT_TYPE = "Tipo incorreto para o ";
	private static final String PARAMETER_IN = "parâmetro \"%s\" no ";

	public static final String INCORRECT_ATTRIBUTE_TYPE = "O tipo do atributo \"%s\" " + SHOULD_BE;
	public static final String INCORRECT_ATTRIBUTE_MODIFIER = "O modificador do atributo \"%s\" " + SHOULD_BE;
	public static final String MISSING_ATTRIBUTE_IN_CLASS = "O atributo \"%s\" " + NOT_PRESENT;

	public static final String MISSING_CONSTRUCTOR_IN_CLASS = "O construtor \"%s\" não foi implementado.\nLembre-se: o Construtor deve ter o exato mesmo nome da classe e não possui tipo de retorno.";
	public static final String INCORRECT_CONSTRUCTOR_MODIFIER = "O modificador do construtor \"%s\" " + SHOULD_BE;

	public static final String MISSING_PARAMETER = "O parâmetro \"%s\" não foi encontrado no ";
	public static final String MISSING_PARAMETER_IN_METHOD = MISSING_PARAMETER + "método \"%s\".";
	public static final String MISSING_PARAMETER_IN_CONSTRUCTOR = MISSING_PARAMETER + "construtor \"%s\".";
	public static final String INCORRECT_PARAMETER_TYPE = INCORRECT_TYPE + PARAMETER_IN;
	public static final String INCORRECT_PARAMETER_TYPE_IN_METHOD = INCORRECT_PARAMETER_TYPE + "método \"%s\". Esperado: %s.";
	public static final String INCORRECT_PARAMETER_TYPE_IN_CONSTRUCTOR = INCORRECT_PARAMETER_TYPE + "construtor \"%s\". Esperado: %s.";

	public static final String INCORRECT_METHOD_RETURN_TYPE = INCORRECT_TYPE + "método \"%s\". Esperado: %s.";
	public static final String INCORRECT_METHOD_MODIFIER = "O modificador do método \"%s\" " + SHOULD_BE;
	public static final String MISSING_METHOD_IN_CLASS = "O método \"%s\" " + NOT_PRESENT;

	public static final String RELATIONSHIP_NOT_IMPLEMENTED = "O relacionameto \"%s\" não foi implementado.";
}

