package org.thraex.toolkit.jpa;

import javax.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

/**
 * @author 鬼王
 * @date 2021/07/22 12:11
 */
public abstract class EnumAttributeConverter<X extends EnumAttributeOperator<Y>, Y> implements AttributeConverter<X, Y> {

    @Override
    public Y convertToDatabaseColumn(X attribute) {
        return Optional.ofNullable(attribute)
                .map(X::value)
                .orElse(null);
    }

    @Override
    public X convertToEntityAttribute(Y value) {
        return X.find(this.getEnumConstants(), value);
    }

    private X[] getEnumConstants() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class actualType = (Class) type.getActualTypeArguments()[0];
        X[] xs = (X[]) actualType.getEnumConstants();

        return xs;
    }

}
