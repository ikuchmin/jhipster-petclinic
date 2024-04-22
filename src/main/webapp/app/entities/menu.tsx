import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/vet">
        <Translate contentKey="global.menu.entities.vet" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/speciality">
        <Translate contentKey="global.menu.entities.speciality" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pet">
        <Translate contentKey="global.menu.entities.pet" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pet-type">
        <Translate contentKey="global.menu.entities.petType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/owner">
        <Translate contentKey="global.menu.entities.owner" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/visit">
        <Translate contentKey="global.menu.entities.visit" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
