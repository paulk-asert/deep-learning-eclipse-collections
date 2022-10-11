import org.eclipse.collections.impl.factory.Lists
import pets.Person
import pets.Pet
import org.eclipse.collections.api.factory.Bags

import static pets.PetType.*

var people = Lists.mutable.with(
        new Person("Mary", "Smith",
                new Pet(CAT, "Tabby", 2)),
        new Person("Bob", "Smith",
                new Pet(CAT, "Dolly", 3),
                new Pet(DOG, "Spot", 2)),
        new Person("Ted", "Smith",
                new Pet(DOG, "Spike", 4)),
        new Person("Jake", "Snake",
                new Pet(SNAKE, "Serpy", 1)),
        new Person("Barry", "Bird",
                new Pet(BIRD, "Tweety", 2)),
        new Person("Terry", "Turtle",
                new Pet(TURTLE, "Speedy", 1)),
        new Person("Harry", "Hamster",
                new Pet(HAMSTER, "Fuzzy", 1),
                new Pet(HAMSTER, "Wuzzy", 1)),
        new Person("John", "Doe", Lists.immutable.<Pet>empty())
)

println GQ {
    from p in people
    select p.fullName, p.pets
}

var counts = people.countByEach(person -> person.petTypes).collect(Object::toString)
var expected = Bags.mutable.withOccurrences("🐱", 2, "🐶", 2, "🐹", 2).with("🐍").with("🐢").with("🐦")
assert counts == expected

import ramo.klevis.ml.vg16.PetType as PT
import ramo.klevis.ml.vg16.VG16ForCat

var vg16ForCat = new VG16ForCat().tap{ loadModel() }
var results = []
people.each{ p ->
    results << p.pets.collect { pet ->
        var file = new File("resources/${pet.type.name()}.png")
        PT petType = vg16ForCat.detectCat(file, 0.675d)
        var desc = switch(petType) {
            case PT.CAT -> 'is a cat'
            case PT.DOG -> 'is a dog'
            default -> 'is unknown'
        }
        "$pet.name $desc"
    }
}
println results.flatten().join('\n')
