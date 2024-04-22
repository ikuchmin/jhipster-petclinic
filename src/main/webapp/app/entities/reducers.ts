import vet from 'app/entities/vet/vet.reducer';
import speciality from 'app/entities/speciality/speciality.reducer';
import pet from 'app/entities/pet/pet.reducer';
import petType from 'app/entities/pet-type/pet-type.reducer';
import owner from 'app/entities/owner/owner.reducer';
import visit from 'app/entities/visit/visit.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  vet,
  speciality,
  pet,
  petType,
  owner,
  visit,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
