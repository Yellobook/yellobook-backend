package com.yellobook.core.domain.inventory;

import static com.yellobook.core.error.CoreErrorType.ONLY_ADMIN_CAN_MANIPULATE_INVENTORY;
import static com.yellobook.core.error.CoreErrorType.VIEWER_CANT_ACCESS_INVENTORY;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class InventoryPermission {
    public void viewerCantAccess(TeamMemberRole role) {
        if (role.equals(TeamMemberRole.VIEWER)) {
            throw new CoreException(VIEWER_CANT_ACCESS_INVENTORY);
        }
    }

    public void onlyAdminCanManipulate(TeamMemberRole role) {
        if (!role.equals(TeamMemberRole.ADMIN)) {
            throw new CoreException(ONLY_ADMIN_CAN_MANIPULATE_INVENTORY);
        }
    }
}
